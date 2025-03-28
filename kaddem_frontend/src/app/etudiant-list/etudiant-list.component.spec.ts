import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EtudiantListComponent } from './etudiant-list.component';

describe('EtudiantListComponent', () => {
  let component: EtudiantListComponent;
  let fixture: ComponentFixture<EtudiantListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EtudiantListComponent]
    });
    fixture = TestBed.createComponent(EtudiantListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
