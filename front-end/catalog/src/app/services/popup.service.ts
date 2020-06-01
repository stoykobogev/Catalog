import { Injectable } from '@angular/core';
import { Popup } from '../models/popup.model';
import { BehaviorSubject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class PopupService {

	popup = new Popup();
	visible = new BehaviorSubject(false);

	constructor() { }

	showPopup(options: Popup.Options): void {

		this.popup.cancelButtonVisible = false;
		this.popup.isImagePreview = false;

		this.popup.content = options.content;
		this.popup.callback = options.callback;

		switch (options.type) {
			case Popup.Types.CONFIRMATION:
				this.popup.title = 'Confirmation';
				this.popup.cancelButtonVisible = true;
				break;
			case Popup.Types.ERROR:
				this.popup.title = 'Error occured';
				break;
			case Popup.Types.INFO:
				this.popup.title = 'Info';
				break;
			case Popup.Types.IMAGE:
				this.popup.title = 'Image';
				this.popup.isImagePreview = true;
				break;
			default:
				break;
		}

		if (options.title) {
			this.popup.title = options.title;
		}

		this.visible.next(true);
	}
}
