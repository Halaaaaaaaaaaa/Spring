package com.ezen.view;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.ezen.biz.dto.AdminVO;
import com.ezen.biz.dto.OrderVO;
import com.ezen.biz.dto.ProductVO;
import com.ezen.biz.service.AdminService;
import com.ezen.biz.service.OrderService;
import com.ezen.biz.service.ProductService;

import utils.Criteria;
import utils.PageMaker;

@Controller
@SessionAttributes("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;
	@Autowired
	private ProductService productService;
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/admin_login_form")
	public String adminLoginView() {
		
		return "admin/main";
	}
	
	@PostMapping("/admin_login")
	public String adminLogin(AdminVO vo, Model model) {
		// (1) 관리자 ID 인증
		int result = adminService.adminCheck(vo);
		
		// (2) 정상관리자 이면
		//     -- 관리자 정보 조회
		//     -- 상품목록 화면으로 이동
		if (result == 1) {
			model.addAttribute("admin", adminService.getAdmin(vo.getId()));
			
			return "redirect: admin_product_list";
		
		} else {
		
			// (3) 비정상 관리자이면
			//     -- 메시지를 설정하고 로그인화면으로 이동
			if (result == 0 || result == -1) {
				model.addAttribute("message", "아이디, 비밀번호를 확인해 주세요.");
			} 
			
			return "admin/main";
		} 
	}
	
	@GetMapping("/admin_logout")
	public String adminLogout(SessionStatus status) {
		
		status.setComplete();  // 세션 해지
		
		return "admin/main"; 
	}
	
	/* 페이징 처리 전 소스
	@RequestMapping("/admin_product_list")
	public String adminProductList(
			@RequestParam(value="key", defaultValue="") String name,
			Model model) {
		// (1) 전체 상품목록 조회
		List<ProductVO> prodList = productService.getListProduct(name);
		
		// (2) 내장 객체에 상품 목록 저장
		model.addAttribute("productList", prodList);
		model.addAttribute("productListSize", prodList.size());
		
		// (3) 화면 호출: productList.jsp
		return "admin/product/productList";
	}
	*/

	// 페이징 처리 후 소스
	@RequestMapping("/admin_product_list")
	public String adminProductList(
			@RequestParam(value="key", defaultValue="") String name,
			Criteria criteria, Model model) {
		// (1) 페이지별 상품목록 조회
		List<ProductVO> prodList = productService.getListProductWithPaging(criteria, name);
		
		// (2) 화면에 표시할 페이지 버튼 정보 설정
		//페이징을 해주기 위해 PageMaker 객체를 생성해서 criteria 변수를 Cri 객체에 주입해서 
		//페이징 조건을 설정한 후, 그 값을 pageMaker 객체에 주입.
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(criteria);
		pageMaker.setTotalCount(productService.countProductList(name));  // 총 게시글의 수를 저장
		
		// (2) 내장 객체에 상품 목록 저장
		model.addAttribute("productList", prodList);
		model.addAttribute("productListSize", prodList.size());
		model.addAttribute("pageMaker", pageMaker);
		
		// (3) 화면 호출: productList.jsp
		return "admin/product/productList";
	}
	
	@RequestMapping("/admin_product_detail")
	public String adminProductDetail(ProductVO vo, Model model, Criteria cri) {
		String[] kindList = {"", "힐", "부츠", "샌달", "슬리퍼", "스니커즈", "세일"};
		
		ProductVO product = productService.getProduct(vo);
		model.addAttribute("productVO", product);
		int index = Integer.parseInt(product.getKind());
		model.addAttribute("kind", kindList[index]);
		model.addAttribute("criteria", cri);
		
		return "admin/product/productDetail";
	}
	
	// 상품 등록 화면 view
	@GetMapping("/admin_product_write_form")
	public String adminProductWriteView(Model model) {
		String kindList[] = {"힐", "부츠", "샌달", "슬리퍼", "스니커즈", "세일"};
		
		model.addAttribute("kindList", kindList);
		
		return "admin/product/productWrite";
	}
	
	// 상품 등록
	//text는 VO 객체로 담고, 나머지는 별도로 requestParam 객체로 담아 반환
	@PostMapping("/admin_product_write")
	public String adminProductWriteAction(ProductVO vo, HttpSession session,
				@RequestParam(value="product_image") MultipartFile uploadFile) {
		System.out.println("=====[ adminProductWriteAction() 호출 성공 ]=====");
		
		//업로드 파일이 업로드 되었는지 확인
		if(!uploadFile.isEmpty()) {
			System.out.println(">>> if문 통과");
			
			//업로드 되었으면 파일명을 추출해서 vo 객체에 파일 네임을 넣어준다
			String fileName = uploadFile.getOriginalFilename();
			vo.setImage(fileName);
			
			//이미지 저장할 위치 지정 : WEB-INF/resources/product_images 폴더에 session 객체에 담아서 저장
			//getServletContext(): 프로젝트 관련한 정보를 리턴
			//getRealPath(): 지정된 경로의 실제 디스크 상의 위치를 알려줌
			//아래의 코드는 결국 프로젝트 관련한 정보를 저장해서 지정한 경로로 리턴하기 위해서 image_path 객체에 저장
			String image_path = session.getServletContext()
		               .getRealPath("WEB-INF/resources/product_images/");

			System.out.println("[image_path] = " + image_path);
			
			//파일 경로로 전송
			try {
				uploadFile.transferTo(new File(image_path + fileName));
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}

		//입력한 상품 정보 저장
		productService.insertProduct(vo);	

		System.out.println("[vo] = " + vo);
		return "redirect:admin_product_list";
	}
	
	// 상품 정보 디테일 내 수정 버튼 - 상품 수정 화면 구현
	@GetMapping("/admin_product_update_form")
	public String adminProductUpdateView(ProductVO vo, Model model) {
		System.out.println("=====[ adminProductUpdateView() 호출 성공 ]=====");
		
		String kindList[] = {"힐", "부츠", "샌달", "슬리퍼", "스니커즈", "세일"};
		
		// 1) 상품 상세 조회
		ProductVO product = productService.getProduct(vo);
		
		// 2) Model 객체에 상품 데이터 저장
		model.addAttribute("productVO", product);
		model.addAttribute("kindList", kindList);
		
		// 3) 화면 호출
		return "admin/product/productUpdate";
	}
	
	//상품 정보 디테일 내 수정버튼 - 상품 수정 구현
	//productUpdate.jsp 내 nonmakeImg, product_image 변수가 vo에 선언되지 않았기 때문에 파라미터를 별도로 입력
	@PostMapping("/admin_product_update")
	public String adminProductUpdate(ProductVO vo, HttpSession session,
				@RequestParam(value="nonmakeImg") String org_image,
				@RequestParam(value="product_image") MultipartFile uploadFile) {
		System.out.println("=====[ adminProductUpdate() 호출 성공 ]=====");
		
		//업로드 파일이 업로드 되었는지 확인
		if(!uploadFile.isEmpty()) { // if문: 상품 이미지가 변경된 경우
			System.out.println(">>> if문 통과");
			
			//업로드 되었으면 파일명을 추출해서 vo 객체에 파일 네임을 넣어준다
			String fileName = uploadFile.getOriginalFilename();
			vo.setImage(fileName);
			
			//이미지 저장할 위치 지정 : WEB-INF/resources/product_images 폴더에 session 객체에 담아서 저장
			//getServletContext(): 프로젝트 관련한 정보를 리턴
			//getRealPath(): 지정된 경로의 실제 디스크 상의 위치를 알려줌
			//아래의 코드는 결국 프로젝트 관련한 정보를 저장해서 지정한 경로로 리턴하기 위해서 image_path 객체에 저장
			String image_path = session.getServletContext()
		               .getRealPath("WEB-INF/resources/product_images/");
				System.out.println("[image_path] = " + image_path);
			
			//파일 경로로 전송
			try {
				uploadFile.transferTo(new File(image_path + fileName));
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		} else { // else문: 기존 이미지 사용
			vo.setImage(org_image);
		}
		
		// 베스트 상품, 신규 상품
        System.out.println("수정 전)useyn = " + vo.getUseyn());
        System.out.println("수정 전)bestyn = " + vo.getBestyn());
        if(vo.getUseyn() == null || vo.getUseyn().equals("n")) {
           
           vo.setUseyn("n");
        }else {
        
           vo.setUseyn("y");
        }
        
        if(vo.getBestyn() == null || vo.getBestyn().equals("n")) {
           
           vo.setBestyn("n");
        }else {
        
           vo.setBestyn("y");
        }
		
		System.out.println("수정 후)useyn = " + vo.getUseyn());
		System.out.println("수정 후)bestyn = " + vo.getBestyn());
		
		productService.updateProduct(vo);
		
		return "redirect:admin_product_list";
	}
	
	// 주문 전체 조회
	@RequestMapping("/admin_order_list")
	public String adminOrderList(Model model,
				@RequestParam(value="key", defaultValue="") String mname) {
		System.out.println("=====[ adminOrderList() 호출 성공 ]=====");
		
		List<OrderVO> orderList = orderService.getListOrder(mname);
		
		model.addAttribute("orderList", orderList);
		
		return "/admin/order/orderList";
	}
	
	//주문 상태 갱신 매핑 - 주문 완료 처리
	@RequestMapping("admin_order_save")
	public String adminOrderSave(OrderVO vo, HttpSession session,
			@RequestParam(value="result") int oseq[]) {
		System.out.println("=====[ adminOrderSave() 호출 성공 ]=====");
		
		for(int i : oseq) {
			orderService.updateOrderResult(i);
		}
		
		return "redirect:admin_order_list";
	}
	
}

