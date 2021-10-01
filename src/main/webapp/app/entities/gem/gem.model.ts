import { IGuard } from 'app/entities/guard/guard.model';
import { IDepartement } from 'app/entities/departement/departement.model';
import { IFrereQuiInvite } from 'app/entities/frere-qui-invite/frere-qui-invite.model';

export interface IGem {
  id?: number;
  nom?: string;
  annee?: string;
  guard?: IGuard | null;
  departement?: IDepartement | null;
  frereQuiInvites?: IFrereQuiInvite[] | null;
  gems?: IGuard[] | null;
}

export class Gem implements IGem {
  constructor(
    public id?: number,
    public nom?: string,
    public annee?: string,
    public guard?: IGuard | null,
    public departement?: IDepartement | null,
    public frereQuiInvites?: IFrereQuiInvite[] | null,
    public gems?: IGuard[] | null
  ) {}
}

export function getGemIdentifier(gem: IGem): number | undefined {
  return gem.id;
}
