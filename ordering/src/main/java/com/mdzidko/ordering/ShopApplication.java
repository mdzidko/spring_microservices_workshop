package com.mdzidko.ordering;

import com.mdzidko.ordering.repositories.*;
import com.mdzidko.ordering.services.CustomersOrdersService;
import com.mdzidko.ordering.services.CustomersService;
import com.mdzidko.ordering.services.ProductsService;

public class ShopApplication {

	public static void main(String[] args) {
		final CustomersRepository customersRepository = new InMemCustomersRepository();
		final ProductsRepository productsRepository = new InMemProductsRepository();
		final CustomersOrdersRepository customersOrdersRepository= new InMemCustomersOrdersRepository();

		final CustomersService customersService = new CustomersService(customersRepository);
		final ProductsService productsService = new ProductsService(productsRepository);
		final CustomersOrdersService customersOrdersService =
				new CustomersOrdersService(customersOrdersRepository, customersRepository, productsRepository);

		final DataInitializer dataInitializer = new DataInitializer(customersService, productsService);
		dataInitializer.initData();
	}
}
