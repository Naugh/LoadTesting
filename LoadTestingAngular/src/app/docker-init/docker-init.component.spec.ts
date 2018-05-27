import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DockerInitComponent } from './docker-init.component';

describe('DockerInitComponent', () => {
  let component: DockerInitComponent;
  let fixture: ComponentFixture<DockerInitComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DockerInitComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DockerInitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
