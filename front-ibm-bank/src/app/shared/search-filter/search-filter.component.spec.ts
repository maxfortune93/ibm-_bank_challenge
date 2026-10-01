import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { SearchFilterComponent } from './search-filter.component';

describe('SearchFilterComponent', () => {
  let component: SearchFilterComponent;
  let fixture: ComponentFixture<SearchFilterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SearchFilterComponent],
      imports: [FormsModule]
    });
    fixture = TestBed.createComponent(SearchFilterComponent);
    component = fixture.componentInstance;
  });

  it('emits only the last term after the debounce time', fakeAsync(() => {
    const emitted: string[] = [];
    component.searchChanged.subscribe((t: string) => emitted.push(t));

    component.searchTerm = 'a';
    component.onSearchChange();
    component.searchTerm = 'ab';
    component.onSearchChange();
    tick(299);
    expect(emitted).toEqual([]);

    tick(1);
    expect(emitted).toEqual(['ab']);
  }));
});
