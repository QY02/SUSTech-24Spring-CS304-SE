import mysql.connector
import sys
import torch
import cornac
import pandas as pd
import logging
import coloredlogs

logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)
fh = logging.FileHandler('ai_rec.log')
ch = logging.StreamHandler()
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
fh.setFormatter(formatter)
ch.setFormatter(formatter)
logger.addHandler(fh)
logger.addHandler(ch)
coloredlogs.install(level='INFO', logger=logger)

from recommenders.datasets.python_splitters import python_random_split
from recommenders.models.cornac.cornac_utils import predict_ranking
from recommenders.utils.timer import Timer
from recommenders.utils.constants import SEED

# top k items to recommend
TOP_K = 5

# Model parameters
LATENT_DIM = 50
ENCODER_DIMS = [100]
ACT_FUNC = "tanh"
LIKELIHOOD = "pois"
NUM_EPOCHS = 500
BATCH_SIZE = 128
LEARNING_RATE = 0.001


if __name__ == '__main__':
    logger.info(f"System version: {sys.version}")
    logger.info(f"PyTorch version: {torch.__version__}")
    logger.info(f"Cornac version: {cornac.__version__}")
    logger.info(f"Device: {torch.cuda.get_device_name(0) if torch.cuda.is_available() else 'CPU'}")
    # Create a connection to the database
    db_connection = mysql.connector.connect(
    host="47.107.113.54",
    user="root",
    password="me#u6236kuGfsv",
    database="events_center_pre"
    )
    cursor = db_connection.cursor()
    
    delete_stmt = "DELETE FROM user_interaction WHERE update_type = 0"
    cursor.execute(delete_stmt)
    db_connection.commit()
    cursor.execute("SELECT * FROM user_interaction")
    results = cursor.fetchall()
    results = [row[:2] + row[3:] for row in results]
    data = pd.DataFrame(
    results,
    columns=["userID", "itemID", "rating"])
    train, test = python_random_split(data, 0.75)
    train_set = cornac.data.Dataset.from_uir(train.itertuples(index=False), seed=SEED)

    logger.info('Number of users: {}'.format(train_set.num_users))
    logger.info('Number of items: {}'.format(train_set.num_items))
    
    bivae = cornac.models.BiVAECF(
    k=LATENT_DIM,
    encoder_structure=ENCODER_DIMS,
    act_fn=ACT_FUNC,
    likelihood=LIKELIHOOD,
    n_epochs=NUM_EPOCHS,
    batch_size=BATCH_SIZE,
    learning_rate=LEARNING_RATE,
    seed=SEED,
    use_gpu=torch.cuda.is_available(),
    verbose=True
    )

    with Timer() as t:
        bivae.fit(train_set)
    logger.info("Took {} seconds for training.".format(t))
    
    with Timer() as t:
        all_predictions = predict_ranking(bivae, data, usercol='userID', itemcol='itemID', remove_seen=True)
        # logger.info(all_predictions)
        all_predictions = all_predictions.sort_values('prediction', ascending=False)
        top_5_predictions = all_predictions.groupby('userID').head(TOP_K)
        data_tuples = [(x[0], x[1]) for x in top_5_predictions[['userID', 'itemID']].to_numpy()]
        insert_query = "INSERT INTO user_interaction (user_id, event_id, rating, update_type) VALUES (%s, %s, 5,0)"
        cursor.executemany(insert_query, data_tuples)
        db_connection.commit()
        
    logger.info("Took {} seconds for prediction.".format(t))

    db_connection.close()