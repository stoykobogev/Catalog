import { Component, OnInit } from '@angular/core';
import { ProductService } from 'src/app/services/product.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Category } from 'src/app/models/category.model';
import { CategoryService } from 'src/app/services/category.service';
import { ProductForm } from 'src/app/models/product-form.model';
import { Product } from 'src/app/models/product.model';
import { forkJoin } from 'rxjs';

@Component({
	selector: 'app-edit-product',
	templateUrl: './edit-product.component.html'
})
export class EditProductComponent implements OnInit {

	category: Category;
	product: Product;
	form: ProductForm;

	constructor(private productService: ProductService, private route: ActivatedRoute, private router: Router,
		private categoryService: CategoryService) { }

	ngOnInit(): void {

		this.category = history.state.category;
		this.product = history.state.product;

		if (!(this.category && this.product)) {
			this.route.params.subscribe((params) => {

				const categoryId = params['categoryId'] as number;
				const productId = params['productId'] as number;

				forkJoin(
					this.categoryService.getCategory(categoryId),
					this.productService.getProduct(productId),
					this.productService.getProductImage(productId)
				).subscribe(([category, product, image]) => {

					this.category = category;
					this.product = product;

					this.form = new ProductForm(category.id, {
						name: product.name,
						price: product.price,
						image: image
					});
				});	
			});
		} else {
			this.productService.getProductImage(this.product.id).subscribe((image) => {
				this.form = new ProductForm(this.category.id, {
					name: this.product.name,
					price: this.product.price,
					image: image
				});
			});
		}
	}

	submit(): void {

		if (this.form.isInvalid()) {
			this.form.markControlsAsDirty();
			return;
		}

		this.productService.editProduct(this.product.id, this.form.getParams()).subscribe(
			() => {
				this.router.navigate(['categories', this.category.id, 'products']);
			}
		);
	}

}
