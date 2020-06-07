import { FormGroup, FormControl, Validators, ValidationErrors } from '@angular/forms';

export class ProductForm extends FormGroup {

	private readonly ALLOWED_IMAGE_TYPES = ['image/jpeg', 'image/png', 'image/webp'];

	categoryId: number;
	name: string;
	price: number;
	image: File;

	constructor(categoryId: number, values?: { name: string, price: number, image: Blob }) {
		super({
			name: new FormControl('', [Validators.required, Validators.maxLength(20)]),
			price: new FormControl('', [Validators.required, Validators.min(0.01), Validators.pattern(/^\d{1,7}(\.\d{1,2})?$/)]),
			image: new FormControl(null, (control: FormControl): ValidationErrors | null => {
				
				const file = control.value as File;

				if (file) {
					if (this.ALLOWED_IMAGE_TYPES.indexOf(file.type) === -1) {
						return { type: true };
					}
				}

				return null;
			}),
		},
		{
			updateOn: 'submit'
		});

		if (values) {
			this.controls.name.setValue(values.name);
			this.controls.price.setValue(values.price);
			this.controls.image.setValue(values.image);
		}

		this.categoryId = categoryId;
	}

	isInvalid(): boolean {
		return !this.valid;
	}

	markControlsAsDirty(): void {
		Object.keys(this.controls).forEach((key) => {
			this.controls[key].markAsDirty();
		})
	}

	getParams(): any {
		return {
			categoryId: this.categoryId,
			name: this.controls.name.value,
			price: this.controls.price.value,
			image: this.controls.image.value
		};
	}
}