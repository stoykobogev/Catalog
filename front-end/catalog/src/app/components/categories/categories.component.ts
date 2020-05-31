import { Component, OnInit } from '@angular/core';
import { Category } from 'src/app/models/category.model';
import { CategoryService } from 'src/app/services/category.service';
import { Roles } from 'src/app/constants/roles';
import { PopupService } from 'src/app/services/popup.service';
import { Popup } from 'src/app/models/popup.model';

@Component({
	selector: 'app-categories',
	templateUrl: './categories.component.html',
	styleUrls: ['./categories.component.css']
})
export class CategoriesComponent implements OnInit {

	categories: Array<Category>;
	roles = Roles;

	constructor(private categoryService: CategoryService, private popupService: PopupService) { }

	ngOnInit(): void {
		this.categoryService.getAllCategories().subscribe(
			(categories) => {
				this.categories = categories;
			}
		);
	}

	viewCategoryProducts(): void {

	}

	editCategory(): void {

	}

	deleteCategory(category: Category): void {
		this.popupService.showPopup({
			type: Popup.Types.CONFIRMATION,
			text: 'Deleting this category will also delete all products in it',
			callback: () => {
				this.categoryService.deleteCategory(category.id).subscribe();
			}
		});
	}
}
