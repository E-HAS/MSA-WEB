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
import com.ehas.lotto.order.dto.OrderItemDto;
import com.ehas.lotto.order.service.OrderItemService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderItemRestController {
	private final OrderItemService orderItemService;

    @GetMapping("{orderSeq}/items")
    public ResponseEntity<ResponseDto> getAll(@RequestParam(value="seq", required=false) Integer seq,
    											@PathVariable(value="orderSeq") Integer orderSeq,
							    				@RequestParam(value="productSeq", required=false) Integer productSeq,
												@RequestParam(value="page", defaultValue = "0") Integer page,
											    @RequestParam(value="size", defaultValue = "10") Integer size,
											    @RequestParam(value="sort", defaultValue = "seq,desc") String[] sort) {
    	try {
        	Sort.Order direction = sort[1].equalsIgnoreCase("desc") ? Sort.Order.desc(sort[0]) : Sort.Order.asc(sort[0]);
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction));
            
            Page<OrderItemDto> lists = orderItemService.findAllBySpecAndPageable(seq, orderSeq, productSeq, pageable);
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
    
    @PostMapping("{orderSeq}/items")
    public ResponseEntity<ResponseDto> add(@PathVariable("orderSeq") Integer orderSeq,
    										@RequestBody OrderItemDto dto) {
    	try {
    		dto.setOrderSeq(orderSeq);
    		ResponseEntity.ok(orderItemService.add(dto));
    		return ResponseEntity.status(HttpStatus.CREATED)
		            .body(ResponseDto.builder()
		                    .status(HttpStatus.CREATED.value())
		                    .message(HttpStatus.CREATED.getReasonPhrase())
		                    .build());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
		}
    }

    @GetMapping("{orderSeq}/items/{seq}")
    public ResponseEntity<ResponseDto> get(@PathVariable("orderSeq") Integer orderSeq
    										,@PathVariable("seq") Integer seq) {
    	try {
    		OrderItemDto dto = orderItemService.findBySeq(seq);
	    	return ResponseEntity.status(HttpStatus.OK)
		            .body(ResponseDto.builder()
		                    .status(HttpStatus.OK.value())
		                    .message(HttpStatus.OK.getReasonPhrase())
		                    .data(Map.of("item", dto))
		                    .build());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(ResponseDto.builder()
	                        .status(HttpStatus.BAD_REQUEST.value())
	                        .message(e.getMessage())
	                        .build());
		}
    }

    @DeleteMapping("{orderSeq}/items/{seq}")
    public ResponseEntity<ResponseDto> delete(@PathVariable("orderSeq") Integer orderSeq
    										 ,@PathVariable("seq") Integer seq) {
    	try {
    		orderItemService.deleteBySeq(seq);
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
