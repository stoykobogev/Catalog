import { Component, OnInit } from '@angular/core';
import { ProductService } from 'src/app/services/product.service';
import { PopupService } from 'src/app/services/popup.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Category } from 'src/app/models/category.model';
import { Product } from 'src/app/models/product.model';
import { CategoryService } from 'src/app/services/category.service';
import { Popup } from 'src/app/models/popup.model';
import { Roles } from 'src/app/constants/roles';

@Component({
	selector: 'app-products',
	templateUrl: './products.component.html',
	styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {

	category: Category;
	products: Product[];
	roles = Roles;

	constructor(private popupService: PopupService, private router: Router, private categoryService: CategoryService,
		private productService: ProductService, private route: ActivatedRoute) { }

	ngOnInit(): void {

		this.category = history.state.category;

		if (this.category) {
			this.productService.getProductsByCategoryId(this.category.id).subscribe((products) => {
				this.products = products;
			});
		} else {
			this.route.queryParams.subscribe((params) => {

				let categoryId = params['categoryId'] as number;

				this.categoryService.getCategory(categoryId).subscribe((category) => {
					this.category = category;
				});

				this.productService.getProductsByCategoryId(categoryId).subscribe((products) => {
					this.products = products;
				});
			});
		}
	}

	showProductImage(product: Product): void {
		this.popupService.showPopup({
			type: Popup.Types.IMAGE,
			content: [this.productService.getProductImageUrl(product.id)]
		});
	}

	deleteProduct(product: Product): void {
		this.popupService.showPopup({
			type: Popup.Types.CONFIRMATION,
			content: ['Are you sure you want to delete this product?'],
			callback: () => {
				this.productService.deleteProduct(product.id).subscribe();
			}
		});
	}
}
