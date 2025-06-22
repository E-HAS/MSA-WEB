package com.ehas.lotto.product.api;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ehas.lotto.common.dto.ResponseDto;
import com.ehas.lotto.product.dto.ProductDto;
import com.ehas.lotto.product.service.ProductService;

import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ResponseDto> getAll(@RequestParam(value="seq", required=false) Integer seq,
									    		@RequestParam(value="name", required=false) String name,
									    		@RequestParam(value="seq", required=false) Integer status,
												@RequestParam(value="stCreatedDate", required=false) String stCreatedDate,
												@RequestParam(value="enCreatedDate", required=false) String enCreatedDate,
												@RequestParam(value="page", defaultValue = "0") Integer page,
											    @RequestParam(value="size", defaultValue = "10") Integer size,
											    @RequestParam(value="sort", defaultValue = "seq,desc") String[] sort) {
    	try {
			Sort.Order direction = sort[1].equalsIgnoreCase("desc") ? Sort.Order.desc(sort[0]) : Sort.Order.asc(sort[0]);
	        Pageable pageable = PageRequest.of(page, size, Sort.by(direction));
	        
	        Page<ProductDto> lists = productService.findAllBySpecAndPageable(seq, name, status, stCreatedDate, enCreatedDate, pageable);
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
    public ResponseEntity<ResponseDto> add(@RequestBody ProductDto dto) {
    	try {
        	productService.add(dto);
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
    @GetMapping("/{seq}")
    public ResponseEntity<ResponseDto> get(@PathVariable Integer seq) {
    	try {
	        ProductDto dto = productService.findBySeq(seq);
	    	return ResponseEntity.status(HttpStatus.CREATED)
		            .body(ResponseDto.builder()
		                    .status(HttpStatus.CREATED.value())
		                    .message(HttpStatus.CREATED.getReasonPhrase())
		                    .data(Map.of("product", dto))
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
    		productService.deleteBySeq(seq);
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
