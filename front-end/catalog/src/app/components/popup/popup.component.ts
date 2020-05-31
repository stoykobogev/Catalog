import { Component, OnInit, AfterViewInit } from '@angular/core';
import { PopupService } from 'src/app/services/popup.service';
import { Popup } from 'src/app/models/popup.model';

@Component({
	selector: 'app-popup',
	templateUrl: './popup.component.html'
})
export class PopupComponent implements OnInit, AfterViewInit {

	popup: Popup;
	$popupEle: any;

	constructor(private popupService: PopupService) { }

	ngOnInit(): void {
		this.popup = this.popupService.popup;

		this.popupService.visible.subscribe((visible) => {
			if (visible) {
				this.$popupEle.modal('show');
			}
		});
	}

	ngAfterViewInit(): void {
		this.$popupEle = window['jQuery']('#popup');
	}

	ok(): void {
		if (this.popup.callback) {
			this.popup.callback();
		}
	}
}
