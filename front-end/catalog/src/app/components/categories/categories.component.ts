import { Component, OnInit } from '@angular/core';
import { Category } from 'src/app/models/category.model';
import { CategoryService } from 'src/app/services/category.service';
import { Roles } from 'src/app/constants/roles';
import { PopupService } from 'src/app/services/popup.service';
import { Popup } from 'src/app/models/popup.model';
import { Router } from '@angular/router';

@Component({
	selector: 'app-categories',
	templateUrl: './categories.component.html',
	styleUrls: ['./categories.component.css']
})
export class CategoriesComponent implements OnInit {

	categories: Array<Category>;
	roles = Roles;

	constructor(private categoryService: CategoryService, 
		private popupService: PopupService, private router: Router) { }

	ngOnInit(): void {
		this.categoryService.getAllCategories().subscribe(
			(categories) => {
				this.categories = categories;
			}
		);
	}

	viewCategoryProducts(category: Category): void {
		this.router.navigate(['categories', category.id, 'products'], 
			{
				state: {
					category: category
				}
			});
	}

	editCategory(): void {

	}

	deleteCategory(category: Category): void {
		this.popupService.showPopup({
			type: Popup.Types.CONFIRMATION,
			content: ['Deleting this category will also delete all products in it.', 'Are you sure?'],
			callback: () => {
				this.categoryService.deleteCategory(category.id).subscribe();
			}
		});
	}
}
