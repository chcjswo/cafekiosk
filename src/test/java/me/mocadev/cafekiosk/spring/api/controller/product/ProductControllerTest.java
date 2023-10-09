package me.mocadev.cafekiosk.spring.api.controller.product;

import static me.mocadev.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static me.mocadev.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.mocadev.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import me.mocadev.cafekiosk.spring.api.service.product.ProductService;
import me.mocadev.cafekiosk.spring.api.service.product.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@Autowired
	private ObjectMapper objectMapper;

	@DisplayName("신규 상품을 등록한다.")
	@Test
	void test1() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(HANDMADE)
			.sellingStatus(SELLING)
			.name("아메리카노")
			.price(4000)
			.build();

		// when
		// then
		mockMvc.perform(post("/api/v1/products")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@DisplayName("신규 상품을 등록 시 상품 타입은 필수값이다.")
	@Test
	void test2() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.sellingStatus(SELLING)
			.name("아메리카노")
			.price(4000)
			.build();

		// when
		// then
		mockMvc.perform(post("/api/v1/products")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value(400))
			.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("상품 타입을 필수입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@DisplayName("판맨 상품을 조회한다.")
	@Test
	void test3() throws Exception {
		// given
		List<ProductResponse> response = List.of() ;

		when(productService.getSellingProduct()).thenReturn(response);

		// when
		// then
		mockMvc.perform(get("/api/v1/products/selling"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.status").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"))
			.andExpect(jsonPath("$.data").isArray())
		;
	}
}
