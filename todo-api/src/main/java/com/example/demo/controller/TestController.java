package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.TodoService;
import com.example.demo.vo.TodoVO;

@RestController
public class TestController {
	@Autowired
	private TodoService todoService;

	@GetMapping("/hello")
	public String helloGet(@RequestParam(name = "title") String title) {
		System.out.println("TestController.helloGet()");
		System.out.println("title -> " + title);
		return "Get Hello World ";
	}

	@PostMapping("/hello")
	public String helloPost(@RequestParam(name = "title") String title) {

		System.out.println("TestController.helloPost()");
		System.out.println("title -> " + title);

		return "Post Hello World";
	}

	/*
	// 입력
	@GetMapping("/todoSampleDataGen")
	public String todoSampleDataGen() {
		System.out.println("TestController.todoSampleDataGen()");
		todoService.bulkInsert();
		return "OK";
	}
	 */
	
	// 출력
	@GetMapping("/todoList")
	public ResponseEntity<List<TodoVO>> todoList() {
		System.out.println("TestController.todoList()");
		List<TodoVO> todoList = todoService.todoList();
		return ResponseEntity.status(HttpStatus.OK).body(todoList);
	}

	// 삭제
	@PostMapping("/delete")
	public ResponseEntity<Integer> delete(@RequestBody TodoVO todoVO) {
		System.out.println("TestController.delete()");
		return ResponseEntity.status(HttpStatus.OK).body(todoService.delete(todoVO));
	}

	// 수정
	@PostMapping("/insert")
	public ResponseEntity<Integer> insert(@RequestBody TodoVO todoVO) {
		System.out.println("TestController.insert()");
		if (todoService.insert(todoVO) >= 0) {
			System.out.println(todoVO);
			return ResponseEntity.status(HttpStatus.OK).body(todoVO.getId());
		}
		return ResponseEntity.status(HttpStatus.OK).body(-1);
	}

	// 입력
	@PostMapping("/update")
	public ResponseEntity<Integer> update(@RequestBody TodoVO todoVO) {
		System.out.println("TestController.update()");
		return ResponseEntity.status(HttpStatus.OK).body(todoService.update(todoVO));
	}
}
