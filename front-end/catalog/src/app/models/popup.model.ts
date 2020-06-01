export class Popup {
	title: string;
	content: string[];
	cancelButtonVisible = false;
	isImagePreview = false;
	callback: () => void;
}

export namespace Popup {

	export enum Types {
		ERROR, CONFIRMATION, INFO, IMAGE
	}

	export class Options {
		type: Popup.Types;
		title?: string;
		content: string[];
		callback?: () => void;
	}
}