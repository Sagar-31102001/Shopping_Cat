package com.ecom.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.service_impl.CategoryServiceImpl;
import com.ecom.service_impl.ProductServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController 
{
	@Autowired
	private CategoryServiceImpl serviceImpl;
	
	@Autowired
	private ProductServiceImpl prdServiceImpl;
	
	@GetMapping("/")
	public String index()
	{
		return "admin/index";
	}
	
	
	// Start Category Operation
	
	@GetMapping("/category")
	public String addCategory(Model m)
	{
		m.addAttribute("category", serviceImpl.getAllCategory());
		return "admin/category";
	}
	
	
	@PostMapping("/saveCategory")
	public String saveCategory(@ModelAttribute Category category,@RequestParam MultipartFile file,HttpSession session) throws IOException
	{
		String imageName= file !=null ? file.getOriginalFilename() : "default.jpg";
		category.setImagename(imageName);
		
		if (serviceImpl.existCategory(category.getName())) 
		{
			session.setAttribute("errorMsg", "Category Already Exists...!!");
		} 
		else
		{
			System.out.println(category);
			Category saveCategory = serviceImpl.saveCategory(category);
			if (ObjectUtils.isEmpty(saveCategory)) 
			{
				session.setAttribute("errorMsg", "Not Saved ! Internal Server Error");
			} 
			else 
			{
				File saveFile = new ClassPathResource("static/img").getFile();
				
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"category_img"+File.separator+imageName);
				
				System.out.println(path);
				
				Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
				
				session.setAttribute("succMsg", "Saved Successfully");
			}
			
		}
		return "redirect:/admin/category";
	}
	
	@GetMapping("/deleteCategory/{id}")
	public String deleteCategory(@PathVariable int id,HttpSession session)
	{
		boolean value = serviceImpl.deleteCategory(id);
		
		if(value)
		{
			session.setAttribute("succMsg", "Delete Successfully...");
		}
		else
		{
			session.setAttribute("errorMsg", "Something Went Wrong...");
		}
		return "redirect:/admin/category";
	}
	
	
	@GetMapping("/loadEditCategory/{id}")
	public String loadEditCategory(@PathVariable int id ,Model m)
	{
		Category category = serviceImpl.getCategory(id);
		m.addAttribute("category", category);
		return "admin/edit_category";
	}
	
	@PostMapping("/updateCategory")
	public String updateCategory(@ModelAttribute Category category, 
	                              @RequestParam Integer id, // Explicitly get the id here
	                              @RequestParam MultipartFile file,
	                              HttpSession session) throws IOException {
	    System.out.println("AdminController.updateCategory()");

	    // Use the id directly if it's missing in the category
	    category.setId(id);

	    

	    Category oldCategory = serviceImpl.getCategory(category.getId());
	    String imageName = file.isEmpty() ? oldCategory.getImagename() : file.getOriginalFilename();
	    
	   
	    System.out.println("Before Update Profile " + oldCategory);
	    
	    if (!ObjectUtils.isEmpty(oldCategory)) {
	        oldCategory.setName(category.getName());
	        oldCategory.setImagename(imageName);
	    }

	    Category saveCategory = serviceImpl.saveCategory(oldCategory);

	    if (!ObjectUtils.isEmpty(saveCategory))
	    {
	    	if (!file.isEmpty()) {
				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
						+ file.getOriginalFilename());

				// System.out.println(path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
	        session.setAttribute("succMsg", "Category update success");
	        
	    } else {
	        session.setAttribute("errorMsg", "Something went wrong on the server");
	    }

	    System.out.println("Update Profile " + saveCategory);

	    return "redirect:/admin/loadEditCategory/" + category.getId();
	}

	// End Category Operation
	
	// Start Product Operation
	
	@GetMapping("/loadAddProduct")
	public String loadAddProduct(Model m)
	{
		List<Category> allCategory = serviceImpl.getAllCategory();
		m.addAttribute("categories", allCategory);
		return "admin/add_product";
	}
	
	@GetMapping("/viewproduct")
	public String viewProducts(Model m,
			@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "2") Integer pageSize
			)
	{
			List<Product> allProducts = prdServiceImpl.getAllProducts();
			m.addAttribute("allproduct", allProducts);
		
		/*
	        // Fetch paginated products from service
	        Page<Product> productPage = prdServiceImpl.getAllProductsPagination(pageNo, pageSize);

	        int totalPages = productPage.getTotalPages();
	        boolean isFirst = productPage.isFirst();
	        boolean isLast = productPage.isLast();

	        // Add attributes to the model to be passed to 
	        m.addAttribute("productPage", productPage.getContent());  // List of products for current page
	        m.addAttribute("totalPages", totalPages);  // Total pages available
	        m.addAttribute("pageNo", pageNo);  // Current page number
	        m.addAttribute("isFirst", isFirst);  // Check if current page is the first
	        m.addAttribute("isLast", isLast);  // Check if current page is the last
		*/
		
		return "admin/products";
	}
	
	@PostMapping("/saveProduct")
	public String saveProduct(@ModelAttribute Product product,@RequestParam MultipartFile file,HttpSession session) throws IOException
	{
		String imageName= file !=null ? file.getOriginalFilename() : "default.jpg";
		
		product.setImage(imageName);
		
		if (prdServiceImpl.existProduct(product.getTitle())) 
		{
			session.setAttribute("errorMsg", "Product Already Exists...!!");
		} 
		else
		{
			System.out.println(product);
			Product saveProduct = prdServiceImpl.saveProduct(product);
			if (ObjectUtils.isEmpty(saveProduct)) 
			{
				session.setAttribute("errorMsg", "Not Saved ! Internal Server Error");
			} 
			else 
			{
				File saveFile = new ClassPathResource("static/img").getFile();
				
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"product_img"+File.separator+imageName);
				
				System.out.println(path);
				
				Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
				
				session.setAttribute("succMsg", "Saved Successfully");
			}
			
		}
		return "redirect:/admin/loadAddProduct";
	}
	
	@GetMapping("/deleteProduct/{id}")
	public String deleteProduct(@PathVariable int id,HttpSession session)
	{
		Boolean value = prdServiceImpl.deleteProduct(id);
		
		if(value)
		{
			session.setAttribute("succMsg", "Delete Successfully...");
		}
		else
		{
			session.setAttribute("errorMsg", "Something Went Wrong...");
		}
		return "redirect:/admin/viewproduct";
	}
	
	@GetMapping("/loadEditProduct/{id}")
	public String loadEditProduct(@PathVariable int id ,Model m)
	{
		Product product = prdServiceImpl.getProductById(id);
		m.addAttribute("product", product);
		List<Category> allCategory = serviceImpl.getAllCategory();
		m.addAttribute("categories", allCategory);
		return "admin/edit_product";
	}
	
	@PostMapping("/updateProduct")
	public String updateProduct(@ModelAttribute Product product, 
	                              @RequestParam Integer id, 
	                              @RequestParam MultipartFile file,
	                              HttpSession session) throws IOException {
	    System.out.println("AdminController.updateCategory()");

	    // Use the id directly if it's missing in the category
	    product.setId(id);
	    
	    Product oldProduct = prdServiceImpl.getProductById(product.getId());
	    
	    String imageName = file.isEmpty() ? oldProduct.getImage() : file.getOriginalFilename();
	    
	   
	    System.out.println("Before Update Profile " + oldProduct);
	    
	    if (!ObjectUtils.isEmpty(oldProduct)) {
	    	
	    	oldProduct.setTitle(product.getTitle());
	        oldProduct.setImage(imageName);
	        oldProduct.setDescription(product.getDescription());
	        oldProduct.setPrice(product.getPrice());
	        oldProduct.setCategory(product.getCategory());
	        
	        
	    }
	   
	    Product saveProduct = prdServiceImpl.saveProduct(oldProduct);
	    
	    if (!ObjectUtils.isEmpty(saveProduct))
	    {
	    	if (!file.isEmpty()) {
				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
						+ file.getOriginalFilename());

				// System.out.println(path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
	        session.setAttribute("succMsg", "Category update success");
	        
	    } else {
	        session.setAttribute("errorMsg", "Something went wrong on the server");
	    }

	    System.out.println("Update Profile " +saveProduct);

	    return "redirect:/admin/loadEditProduct/" + product.getId();
	}
}
