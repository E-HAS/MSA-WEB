package com.ehas.lotto.lotto.api;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ehas.lotto.common.dto.ResponseDto;
import com.ehas.lotto.lotto.dto.LottoDto;
import com.ehas.lotto.lotto.dto.LottoNumberStatDto;
import com.ehas.lotto.lotto.service.LottoNumberStatService;
import com.ehas.lotto.lotto.service.LottoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stat/lottos")
@RequiredArgsConstructor
public class LottoNumberStatRestController {
	private final LottoNumberStatService lottoNumberStatService;

	@GetMapping
	public ResponseEntity<ResponseDto> getAll(				@RequestParam(value="stRound", required=false) Integer stRound,
															@RequestParam(value="enRound", required=false) Integer enRound,
															@RequestParam(value="number", required=false) Integer number,
															@RequestParam(value="page", defaultValue = "0") Integer page,
														    @RequestParam(value="size", defaultValue = "10") Integer size,
														    @RequestParam(value="sort", defaultValue = "id.round,desc") String[] sort) {
		try {
			Sort.Order direction = sort[1].equalsIgnoreCase("desc") ? Sort.Order.desc(sort[0]) : Sort.Order.asc(sort[0]);
	        Pageable pageable = PageRequest.of(page, size, Sort.by(direction));
	        Page<LottoNumberStatDto> lists = lottoNumberStatService.findAllBySpecAndPageable(stRound, enRound, number, pageable);
	        
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
	public ResponseEntity<ResponseDto> add(@RequestBody LottoNumberStatDto dto) {
		try {
			lottoNumberStatService.add(dto);
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

	@GetMapping("/{round}/number/{number}")
	public ResponseEntity<ResponseDto> get(@PathVariable("round") Integer round
											,@PathVariable("number") Integer number) {
		try {
			LottoNumberStatDto dto = lottoNumberStatService.findByNumber(round, number);
			return ResponseEntity.status(HttpStatus.OK)
		            .body(ResponseDto.builder()
		                    .status(HttpStatus.OK.value())
		                    .message(HttpStatus.OK.getReasonPhrase())
		                    .data(Map.of("number", dto))
		                    .build());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
		}
	}

	@DeleteMapping("/{round}/number/{number}")
	public ResponseEntity<ResponseDto> delete(@PathVariable("round") Integer round
											,@PathVariable("number") Integer number) {
		try {
			lottoNumberStatService.deleteByNumber(round, number);
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
