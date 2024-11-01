package org.cs304.backend.service.impl;

import org.cs304.backend.entity.Comment;
import org.cs304.backend.mapper.CommentMapper;
import org.cs304.backend.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}
