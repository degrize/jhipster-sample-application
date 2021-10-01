import { IQuartier } from 'app/entities/quartier/quartier.model';

export interface IVille {
  id?: number;
  nom?: string;
  quartiers?: IQuartier[] | null;
}

export class Ville implements IVille {
  constructor(public id?: number, public nom?: string, public quartiers?: IQuartier[] | null) {}
}

export function getVilleIdentifier(ville: IVille): number | undefined {
  return ville.id;
}
