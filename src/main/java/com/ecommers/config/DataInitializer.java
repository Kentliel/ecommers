package com.ecommers.config;

import com.ecommers.enums.Role;
import com.ecommers.models.*;
import com.ecommers.repositorys.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner
{
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args)
    {
        if (userRepository.count() > 0)
            return;

        User admin = User.builder().
                firstName("Admin").
                lastName("Ecommerce").
                email("admin@ecommers.com").
                password("admin123").
                enabled(true).
                role(Role.ADMIN).
                build();

        User customer = User.builder().
                firstName("Cliente").
                lastName("Demo").
                email("cliente@ecommers.com").
                password("cliente123").
                role(Role.CUSTOMER).
                enabled(true).
                build();

        userRepository.save(admin);
        userRepository.save(customer);

        Address address = Address.builder()
                .street("Calle Mayor 123")
                .city("Madrid")
                .province("Madrid")
                .postalCode("28013")
                .country("España")
                .defaultAddress(true)
                .user(customer)
                .build();

        addressRepository.save(address);

        Category electronics = Category.builder()
                .name("ELectronica")
                .slug("electronica")
                .description("Productos electronicos, tecnologia y accesorios.")
                .active(true)
                .build();

        Category fashion = Category.builder()
                .name("Moda")
                .slug("moda")
                .description("Ropa, calzado y complementos.")
                .active(true)
                .build();

        categoryRepository.save(electronics);
        categoryRepository.save(fashion);

        Brand apple = Brand.builder()
                .name("Apple")
                .nif("A12345678")
                .country("Estados Unidos")
                .website("https://www.apple.com")
                .active(true)
                .build();

        Brand nike = Brand.builder()
                .name("Nike")
                .nif("B12345678")
                .country("Estados Unidos")
                .website("https://www.nike.com")
                .active(true)
                .build();

        brandRepository.save(apple);
        brandRepository.save(nike);

        Product iphone = Product.builder()
                .sku("ELEC-IPHONE-001")
                .name("iPhone 15")
                .slug("iphone-15")
                .description("Smartphone Apple iPhone 15 con pantalla Super Retina XDR")
                .price(new BigDecimal("899.99"))
                .stock(25)
                .imageUrl("https://example.com/images/iphone-15.jpg")
                .active(true)
                .category(electronics)
                .brand(apple)
                .build();

        Product shoes = Product.builder()
                .sku("MODA-NIKE-001")
                .name("Nike Air Max")
                .slug("nike-air-max")
                .description("Zapatillas deportivas NIKE air MAX para uso diario.")
                .price(new BigDecimal("129.99"))
                .stock(40)
                .imageUrl("https://example.com/images/nike-air-max.jpg")
                .active(true)
                .category(fashion)
                .brand(nike)
                .build();

        productRepository.save(iphone);
        productRepository.save(shoes);
    }

}
