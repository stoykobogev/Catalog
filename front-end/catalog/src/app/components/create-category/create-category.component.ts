import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { CategoryService } from 'src/app/services/category.service';
import { Router } from '@angular/router';

@Component({
	selector: 'app-create-category',
	templateUrl: './create-category.component.html'
})
export class CreateCategoryComponent implements OnInit {

	form: FormGroup;
	nameControl: FormControl;

	constructor(private categoryService: CategoryService, private formBuilder: FormBuilder, private router: Router) { }

	ngOnInit(): void {

		this.nameControl = this.formBuilder.control(
			'', [Validators.required, Validators.maxLength(20)]
		);

		this.nameControl.updateOn

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

		this.categoryService.createCategory(this.nameControl.value)
			.subscribe(() => {
				this.router.navigateByUrl('/categories')
			});
	}
}
