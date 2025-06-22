package com.ehas.lotto.order.api;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ehas.lotto.common.dto.ResponseDto;
import com.ehas.lotto.order.dto.OrderDto;
import com.ehas.lotto.order.service.OrderService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderRestController {
	private final OrderService orderService;
	@GetMapping
	public ResponseEntity<ResponseDto> getAll(	@RequestParam(value="seq", required=false) Integer seq,
													@RequestParam(value="userSeq", required=false) Integer userSeq,
													@RequestParam(value="status", required=false) Integer status,
													@RequestParam(value="stCreatedDate", required=false) String stCreatedDate,
													@RequestParam(value="enCreatedDate", required=false) String enCreatedDate,
													@RequestParam(value="page", defaultValue = "0") Integer page,
												    @RequestParam(value="size", defaultValue = "10") Integer size,
												    @RequestParam(value="sort", defaultValue = "seq,desc") String[] sort) {
		try {
			Sort.Order direction = sort[1].equalsIgnoreCase("desc") ? Sort.Order.desc(sort[0]) : Sort.Order.asc(sort[0]);
	        Pageable pageable = PageRequest.of(page, size, Sort.by(direction));
	        Page<OrderDto> lists = orderService.findAllBySpecAndPageable(seq, userSeq, status, stCreatedDate, enCreatedDate, pageable);
			return ResponseEntity.ok(ResponseDto.builder()
	                .status(HttpStatus.OK.value())
	                .message(HttpStatus.OK.getReasonPhrase())
	                .data(Map.of("list", lists))
	                .build());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(ResponseDto.builder()
	                        .status(HttpStatus.BAD_REQUEST.value())
	                        .message(e.getMessage())
	                        .build());
		}
	}

	@PostMapping
	public ResponseEntity<ResponseDto> add(@RequestBody OrderDto dto) {
		try {
			ResponseEntity.ok(orderService.add(dto));
			return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.builder()
					.status(HttpStatus.CREATED.value()).message(HttpStatus.CREATED.getReasonPhrase()).build());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(ResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).message(e.getMessage()).build());
		}
	}

	@GetMapping("/{seq}")
	public ResponseEntity<ResponseDto> get(@PathVariable Integer seq) {
		try {
			OrderDto dto = orderService.findBySeq(seq);
			return ResponseEntity.status(HttpStatus.CREATED)
		            .body(ResponseDto.builder()
		                    .status(HttpStatus.CREATED.value())
		                    .message(HttpStatus.CREATED.getReasonPhrase())
		                    .data(Map.of("order", dto))
		                    .build());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(ResponseDto.builder()
	                        .status(HttpStatus.BAD_REQUEST.value())
	                        .message(e.getMessage())
	                        .build());
		}
	}

	@DeleteMapping("/{seq}")
	public ResponseEntity<ResponseDto> delete(@PathVariable Integer seq) {
		try {
			orderService.deleteBySeq(seq);
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
		            .body(ResponseDto.builder()
		                    .status(HttpStatus.NO_CONTENT.value())
		                    .message(HttpStatus.NO_CONTENT.getReasonPhrase())
		                    .build());
		}catch(Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		    .body(ResponseDto.builder()
		            .status(HttpStatus.BAD_REQUEST.value())
		            .message(e.getMessage())
		            .build());
		}
	}
}
