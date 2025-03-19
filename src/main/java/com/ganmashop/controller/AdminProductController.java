package com.ganmashop.controller;

import com.ganmashop.entity.Product;
import com.ganmashop.entity.User;
import com.ganmashop.service.ProductService;
import com.ganmashop.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * @author Jasonlzc
 * Date: 01/03/2025
 */

@Controller
@RequestMapping("/admin")
public class AdminProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String showProductPage(Model model, HttpSession session){
        // 添加商品列表到模型
        List<Product> products = productService.findAllProducts();
        model.addAttribute("product", products); // 确保属性名称与模板中的一致

        // 登录状态检查（可选）
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("isLoggedIn", user != null);

        return "admin/products";
    }

    @GetMapping("/products/addProduct")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product()); // 创建空对象
        return "admin/addProduct"; // 修正模板名称
    }

    // 处理添加请求（POST）
    @PostMapping("/products/addProduct")
    public String addProduct(@ModelAttribute Product product, HttpSession session, RedirectAttributes redirectAttributes) {
        // 验证登录状态
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/auth/login";
        }
        try {
            product.setUserId(loggedInUser.getId());
            productService.save(product);
            redirectAttributes.addFlashAttribute("message", "商品添加成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "添加失败: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/products/editProduct/{id}")
    public String showEditForm(@ModelAttribute Product product, Model model) {
        product = productService.findProductById(product.getId());
        model.addAttribute("product", product);
        return "admin/updateProduct";
    }

    @PostMapping("/products/updateProduct/{id}")
    public String updateAccount(@PathVariable String id, @ModelAttribute Product product, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/auth/login";
        }
        try{
            product.setId(id);
            productService.updateProduct(product);
            // Add a success message as a flash attribute
            redirectAttributes.addFlashAttribute("message", "商品更新成功!");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "系统错误，请稍后再试");
        }
        return "redirect:/admin/products"; // Redirect back to the account page
    }

    @GetMapping("/products/deleteProduct/{id}")
    public String deleteUser(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try{
            productService.deleteProductById(id);
            redirectAttributes.addFlashAttribute("message", "商品删除成功！");
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error message", "商品删除失败！");
        }

        return "redirect:/admin/products";
    }
}
