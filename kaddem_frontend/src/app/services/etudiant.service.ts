import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EtudiantService {
  private apiUrl = 'http://192.168.50.4:8082/kaddem/etudiant/retrieve-all-etudiants';

  constructor(private http: HttpClient) {}

  getEtudiants(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
}