package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.vo.TodoVO;

@Mapper
public interface TodoDAO {
	// 입력
	int insert(TodoVO todoVO);
	
	// 출력
	List<TodoVO> todoList();
	
	// 삭제
	int delete(TodoVO todoVO);
	
	// 수정
	int update(TodoVO todoVO);
	
}
