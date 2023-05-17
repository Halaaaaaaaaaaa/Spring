package com.ezen.biz.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezen.biz.dto.ProductVO;

@Repository
public class ProductDAO{
//"product-mapping.xml"���� ������ ȣ���ϴ� �޼ҵ� �ۼ�
	//�޼ҵ���� ���� ID�� ������.
	@Autowired
	SqlSessionTemplate mybatis;
	
	public List<ProductVO> getNewProductList() {
		return mybatis.selectList("ProductMapper.getNewProductList");
	}
	public List<ProductVO> getBestProductList() {
		return mybatis.selectList("ProductMapper.getBestProductList");
	}
	public ProductVO getProduct(ProductVO vo) {
		return mybatis.selectOne("ProductMapper.getProduct", vo);
	}
	
	public List<ProductVO> getProductListByKind(String kind) {
		return mybatis.selectList("ProductMapper.getProductListByKind",kind);
	}
	// �ȳ� !!!!!!!!!!!!!
	
}