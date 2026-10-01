export function formatNumberWithHyphen(value: number | string | null): string {
  if (value === null || value === undefined) {
    return '';
  }
  const valueStr = value.toString();
  if (valueStr.length > 1) {
    const lastDigit = valueStr.slice(-1);
    const restOfNumber = valueStr.slice(0, -1);
    return `${restOfNumber}-${lastDigit}`;
  }
  return valueStr;
}

export const formattedCurrency = (value: number) => {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  }).format(value);
}


export const initials = (name: string | null | undefined): string => {
  const parts = (name ?? '').trim().split(/\s+/).filter(Boolean);
  if (parts.length === 0) {
    return '?';
  }
  const first = parts[0][0];
  const last = parts.length > 1 ? parts[parts.length - 1][0] : '';
  return (first + last).toUpperCase();
};
