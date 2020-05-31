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

		switch (options.type) {
			case Popup.Types.CONFIRMATION:
				this.popup.title = 'Confirmation';
				this.popup.cancelButtonVisible = true;
				break;
			case Popup.Types.ERROR:
				this.popup.title = 'Error occured';
				this.popup.cancelButtonVisible = false;
				break;
			case Popup.Types.INFO:
				this.popup.title = 'Info';
				this.popup.cancelButtonVisible = false;
				break;
			default:
				break;
		}

		if (options.title) {
			this.popup.title = options.title;
		}

		this.popup.text = options.text;
		this.popup.callback = options.callback;

		this.visible.next(true);
	}
}
