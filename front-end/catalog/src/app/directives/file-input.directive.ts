import { Directive, HostListener } from '@angular/core';
import { FormControlName } from '@angular/forms';

@Directive({
	selector: 'input[type="file"]'
})
export class FileInputDirective {

    constructor(private formControlName: FormControlName) { }

    @HostListener('input', ['$event.target.files[0]']) 
    onInputChange(file: File) {
       	this.formControlName.control.setValue(file, {
			   onlySelf: true,
			   emitEvent: false,
			   emitModelToViewChange: false,
			   emitViewToModelChange: false
		   });
    }
}