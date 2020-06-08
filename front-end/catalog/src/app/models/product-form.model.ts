import { FormGroup, FormControl, Validators, ValidationErrors } from '@angular/forms';

export class ProductForm extends FormGroup {

	static readonly ALLOWED_IMAGE_TYPES = ['image/jpeg', 'image/png', 'image/webp'];

	categoryId: number;
	name: string;
	price: number;
	image: Blob;
	encodedImage: string;
	fileReader = new FileReader();

	constructor(categoryId: number, values?: { name: string, price: number, image: Blob }) {
		super({
			name: new FormControl(values ? values.name : '', [Validators.required, Validators.maxLength(20)]),
			price: new FormControl(values ? values.price : '', [Validators.required, Validators.min(0.01), Validators.pattern(/^\d{1,7}(\.\d{1,2})?$/)]),
			image: new FormControl(values ? values.image : null)
		},
		{
			updateOn: 'submit'
		});

		this.controls.image.setValidators((control: FormControl): ValidationErrors | null => {
				
			const file = control.value as Blob;

			if (file) {
				if (ProductForm.ALLOWED_IMAGE_TYPES.indexOf(file.type) === -1) {
					return { type: true };
				} else {
					this.fileReader.onload = (ev: ProgressEvent<FileReader>) => {
						this.encodedImage = ev.target.result as string;
					};

					this.fileReader.readAsDataURL(file);
				}
			}

			return null;
		});

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
			image: this.encodedImage.slice(this.encodedImage.indexOf('base64,') + 7)
		};
	}
}