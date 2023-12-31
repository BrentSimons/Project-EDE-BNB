import { TestBed } from '@angular/core/testing';

import { BnbService } from './bnb.service';

describe('BnbService', () => {
  let service: BnbService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BnbService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
