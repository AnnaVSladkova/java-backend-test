package ru.geekbrains.javaback.utils;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import ru.geekbrains.javaback.db.dao.CategoriesMapper;
import ru.geekbrains.javaback.db.dao.ProductsMapper;
import ru.geekbrains.javaback.db.model.Categories;
import ru.geekbrains.javaback.db.model.CategoriesExample;
import ru.geekbrains.javaback.db.model.Products;
import ru.geekbrains.javaback.db.model.ProductsExample;
import ru.geekbrains.javaback.dto.Product;

import java.io.IOException;
@UtilityClass
public class DbUtils {
    private static String resource = "mybatisConfig.xml";
    static Faker faker = new Faker();
    private static SqlSession getSqlSession() throws IOException {
        SqlSessionFactory sqlSessionFactory;
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream(resource));
        return sqlSessionFactory.openSession(true);
    }
    @SneakyThrows
    public static CategoriesMapper getCategoriesMapper(){
        return getSqlSession().getMapper(CategoriesMapper.class);
    }
    @SneakyThrows
    public static ProductsMapper getProductsMapper() {
        return getSqlSession().getMapper(ProductsMapper.class);
    }
    private static void createNewCategory(CategoriesMapper categoriesMapper) {
        Categories newCategory = new Categories();
        newCategory.setTitle(faker.animal().name());

        categoriesMapper.insert(newCategory);
    }

    public static Integer countCategories(CategoriesMapper categoriesMapper) {
        long categoriesCount = categoriesMapper.countByExample(new CategoriesExample());
        return Math.toIntExact(categoriesCount);
    }

    public static Integer countProducts(ProductsMapper productsMapper) {
        long products = productsMapper.countByExample(new ProductsExample());
        return Math.toIntExact(products);
    }

    public static Product createProducts(ProductsMapper productsMapper, Product product) {
        Products _product = new Products();
        _product.setTitle(product.getTitle());
        _product.setPrice(product.getPrice());
        _product.setCategory_id((long)product.getCategoryId());
        int row_num = productsMapper.insert(_product);
        product.setId(_product.getId().intValue());
        return product;
    }

    public static void updateProduct(ProductsMapper productsMapper, Product product) {
        Products _product = new Products();
        _product.setId((long)product.getId());
        _product.setTitle(product.getTitle());
        _product.setPrice(product.getPrice());
        _product.setCategory_id((long)product.getCategoryId());
        int id = productsMapper.updateByPrimaryKey(_product);
    }

    public static Product getProduct(ProductsMapper productsMapper, Integer id) {
        Products product = productsMapper.selectByPrimaryKey((long)id);
        Product _product = new Product();
        _product.setId(product.getId().intValue());
        _product.setTitle(product.getTitle());
        _product.setPrice(product.getPrice());
        _product.setCategoryId(product.getCategory_id().intValue());
        return _product;
    }

    public static void deleteProduct(ProductsMapper productsMapper, int productId) {
        productsMapper.deleteByPrimaryKey((long)productId);
    }
}


