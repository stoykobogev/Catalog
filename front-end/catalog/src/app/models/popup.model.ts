export class Popup {
	title: string;
	text: string;
	cancelButtonVisible = false;
	callback: () => void;
}

export namespace Popup {

	export enum Types {
		ERROR, CONFIRMATION, INFO
	}

	export class Options {
		type: Popup.Types;
		title?: string;
		text: string;
		callback?: () => void;
	}
}