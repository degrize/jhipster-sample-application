import { IQuartier } from 'app/entities/quartier/quartier.model';
import { IDepartement } from 'app/entities/departement/departement.model';
import { INouveau } from 'app/entities/nouveau/nouveau.model';
import { IGem } from 'app/entities/gem/gem.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';

export interface IFrereQuiInvite {
  id?: number;
  nomComplet?: string;
  contact?: string | null;
  sexe?: Sexe | null;
  quartier?: IQuartier | null;
  departements?: IDepartement[] | null;
  nouveaus?: INouveau[] | null;
  gems?: IGem[] | null;
}

export class FrereQuiInvite implements IFrereQuiInvite {
  constructor(
    public id?: number,
    public nomComplet?: string,
    public contact?: string | null,
    public sexe?: Sexe | null,
    public quartier?: IQuartier | null,
    public departements?: IDepartement[] | null,
    public nouveaus?: INouveau[] | null,
    public gems?: IGem[] | null
  ) {}
}

export function getFrereQuiInviteIdentifier(frereQuiInvite: IFrereQuiInvite): number | undefined {
  return frereQuiInvite.id;
}
