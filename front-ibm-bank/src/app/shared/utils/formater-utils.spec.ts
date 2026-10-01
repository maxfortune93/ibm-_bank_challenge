import { formatNumberWithHyphen, formattedCurrency, initials } from './formater-utils';

describe('formater-utils', () => {
  it('separates the check digit with a hyphen', () => {
    expect(formatNumberWithHyphen('123456')).toBe('12345-6');
    expect(formatNumberWithHyphen(98)).toBe('9-8');
  });

  it('handles empty and single-digit values', () => {
    expect(formatNumberWithHyphen(null)).toBe('');
    expect(formatNumberWithHyphen('7')).toBe('7');
  });

  it('formats values as Brazilian currency', () => {
    expect(formattedCurrency(1234.5).replace(/\s/g, ' ')).toBe('R$ 1.234,50');
  });
});

describe('initials', () => {
  it('uses the first and last name', () => {
    expect(initials('Ana Maria Souza')).toBe('AS');
    expect(initials('bruno')).toBe('B');
  });

  it('falls back to a placeholder', () => {
    expect(initials('  ')).toBe('?');
    expect(initials(null)).toBe('?');
  });
});
