import { IVille } from 'app/entities/ville/ville.model';
import { IFrereQuiInvite } from 'app/entities/frere-qui-invite/frere-qui-invite.model';

export interface IQuartier {
  id?: number;
  nom?: string;
  villes?: IVille[] | null;
  frereQuiInvites?: IFrereQuiInvite[] | null;
}

export class Quartier implements IQuartier {
  constructor(
    public id?: number,
    public nom?: string,
    public villes?: IVille[] | null,
    public frereQuiInvites?: IFrereQuiInvite[] | null
  ) {}
}

export function getQuartierIdentifier(quartier: IQuartier): number | undefined {
  return quartier.id;
}
