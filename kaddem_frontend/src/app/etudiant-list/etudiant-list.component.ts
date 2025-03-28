import { Component, OnInit } from '@angular/core';
import { EtudiantService } from '../services/etudiant.service';

@Component({
  selector: 'app-etudiant-list',
  templateUrl: './etudiant-list.component.html',
  styleUrls: ['./etudiant-list.component.css']
})
export class EtudiantListComponent implements OnInit {
  etudiants: any[] = [];

  constructor(private etudiantService: EtudiantService) {}

  ngOnInit(): void {
    this.etudiantService.getEtudiants().subscribe(
      (data) => {
        this.etudiants = data; // Stocke les données dans la variable
      },
      (error) => {
        console.error('Erreur lors de la récupération des étudiants', error);
      }
    );
  }
}