export interface ICommunaute {
  id?: number;
  nom?: string;
}

export class Communaute implements ICommunaute {
  constructor(public id?: number, public nom?: string) {}
}

export function getCommunauteIdentifier(communaute: ICommunaute): number | undefined {
  return communaute.id;
}
