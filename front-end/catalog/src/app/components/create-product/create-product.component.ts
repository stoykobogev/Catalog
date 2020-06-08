import { Component, OnInit } from '@angular/core';
import { ProductService } from 'src/app/services/product.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Category } from 'src/app/models/category.model';
import { CategoryService } from 'src/app/services/category.service';
import { ProductForm } from 'src/app/models/product-form.model';

@Component({
	selector: 'app-create-product',
	templateUrl: './create-product.component.html'
})
export class CreateProductComponent implements OnInit {

	category: Category;
	form: ProductForm;

	constructor(private productService: ProductService, private route: ActivatedRoute, private router: Router,
		private categoryService: CategoryService) { }

	ngOnInit(): void {

		this.category = history.state.category;

		if (!this.category) {
			this.route.params.subscribe((params) => {

				const categoryId = params['categoryId'] as number;

				this.categoryService.getCategory(categoryId).subscribe((category) => {
					this.category = category;
				});

				this.form = new ProductForm(categoryId);
			});
		} else {
			this.form = new ProductForm(this.category.id);
		}
	}

	submit(): void {

		if (this.form.isInvalid()) {
			this.form.markControlsAsDirty();
			return;
		}

		this.productService.createProduct(this.form.getParams()).subscribe(
			() => {
				this.router.navigate(['categories', this.category.id, 'products']);
			}
		);
	}
}
