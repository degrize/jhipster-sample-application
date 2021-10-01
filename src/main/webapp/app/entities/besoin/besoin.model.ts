import { INouveau } from 'app/entities/nouveau/nouveau.model';
import { BesoinType } from 'app/entities/enumerations/besoin-type.model';

export interface IBesoin {
  id?: number;
  besoin?: BesoinType;
  nouveaus?: INouveau[] | null;
}

export class Besoin implements IBesoin {
  constructor(public id?: number, public besoin?: BesoinType, public nouveaus?: INouveau[] | null) {}
}

export function getBesoinIdentifier(besoin: IBesoin): number | undefined {
  return besoin.id;
}
