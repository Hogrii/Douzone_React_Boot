package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.TodoDAO;
import com.example.demo.vo.TodoVO;

@Service
public class TodoService {
	@Autowired
	private TodoDAO todoDAO;

	// 입력
	public int insert(TodoVO todoVO) {
		return todoDAO.insert(todoVO);
	}

	public void bulkInsert() {
		TodoVO todoVO = new TodoVO();
		for(int i=0; i<2500; i++) {
			todoVO.setTitle("리액트 기초 알아보기 할일 " + i);
			todoVO.setChecked(i % 3 == 0 ? 'T' : 'F');
			todoDAO.insert(todoVO);
		}
	}
	
	// 출력
	public List<TodoVO> todoList() {
		return todoDAO.todoList();
	}
	
	// 삭제
	public int delete(TodoVO todoVO) {
		return todoDAO.delete(todoVO);
	}
	
	// 수정
	public int update(TodoVO todoVO) {
		return todoDAO.update(todoVO);
	}
}
