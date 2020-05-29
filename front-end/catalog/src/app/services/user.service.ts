import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import jwt_decode from 'jwt-decode';


@Injectable({
	providedIn: 'root'
})
export class UserService {

	currentUser = new BehaviorSubject<User>(null);

	constructor(private http: HttpClient) { }

	login(username: string, password: string): Observable<HttpResponse<Object>> {
		return this.http.post('/api/login', null, {
			headers: {
				'Content-Type': 'application/x-www-form-urlencoded'
			},
			params: {
				username: username,
				password: password
			},
			observe: 'response'
		})
		.pipe(tap((response) => {
			const jwt = response.headers.get('authorization');
			const token = jwt_decode(jwt)

			let user = new User();
			user.username = token.sub;
			user.roles = token.roles;

			this.currentUser.next(user);
		}));
	}

	logout(): void {
		this.http.get<void>('/api/users/logout').subscribe();
		this.currentUser.next(null);
	}
}
