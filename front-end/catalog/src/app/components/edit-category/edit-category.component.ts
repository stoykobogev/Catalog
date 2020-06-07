import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { CategoryService } from 'src/app/services/category.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Category } from 'src/app/models/category.model';

@Component({
	selector: 'app-edit-category',
	templateUrl: './edit-category.component.html'
})
export class EditCategoryComponent implements OnInit {

	category: Category;
	form: FormGroup;
	nameControl: FormControl;

	constructor(private categoryService: CategoryService, private formBuilder: FormBuilder, private router: Router,
		private route: ActivatedRoute) { }

	ngOnInit(): void {

		this.category = history.state.category;

		this.nameControl = this.formBuilder.control(
			'', [Validators.required, Validators.maxLength(20)]
		);

		if (!this.category) {

			this.route.params.subscribe((params) => {

				console.log(this.route)
				let categoryId = params['categoryId'] as number;

				this.categoryService.getCategory(categoryId).subscribe((category) => {
					this.category = category;

					this.nameControl.setValue(this.category.name);
				});
			});
		} else {
			this.nameControl.setValue(this.category.name);
		}

		this.form = this.formBuilder.group({
			name: this.nameControl
		}, {
			updateOn: 'submit'
		});
	}

	submit(): void {

		if (this.form.invalid) {
			this.nameControl.markAsDirty();
			return;
		}

		this.categoryService.editCategory(this.category.id, this.nameControl.value)
			.subscribe(() => {
				this.router.navigateByUrl('/categories')
			});
	}
}
