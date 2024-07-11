import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-loading-skeleton',
  templateUrl: './loading-skeleton.component.html',
  styleUrl: './loading-skeleton.component.scss'
})
export class LoadingSkeletonComponent {
  @Input() isLoading: boolean = false;
  @Input() loadingMessage: string = 'Carregando dados, por favor aguarde...';
  @Input() count: number = 8;
  @Input() appearance: 'circle' | 'line' | 'custom-content' = 'line';
  @Input() theme: any = { 'background-color': '#777', height: '20px', 'margin-bottom': '10px' };

  constructor() { }
}
