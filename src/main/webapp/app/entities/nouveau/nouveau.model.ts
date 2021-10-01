import * as dayjs from 'dayjs';
import { ICommunaute } from 'app/entities/communaute/communaute.model';
import { IVille } from 'app/entities/ville/ville.model';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { ICulte } from 'app/entities/culte/culte.model';
import { IDepartement } from 'app/entities/departement/departement.model';
import { IFrereQuiInvite } from 'app/entities/frere-qui-invite/frere-qui-invite.model';
import { IBesoin } from 'app/entities/besoin/besoin.model';
import { IDecision } from 'app/entities/decision/decision.model';
import { SituationMatrimoniale } from 'app/entities/enumerations/situation-matrimoniale.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { CanalInvitation } from 'app/entities/enumerations/canal-invitation.model';

export interface INouveau {
  id?: number;
  nomComplet?: string;
  contact?: string | null;
  trancheAge?: string | null;
  situationMatrimoniale?: SituationMatrimoniale | null;
  date?: dayjs.Dayjs | null;
  impressionsDuCulte?: string | null;
  sexe?: Sexe | null;
  invitePar?: CanalInvitation | null;
  communaute?: ICommunaute | null;
  ville?: IVille | null;
  quartier?: IQuartier | null;
  culte?: ICulte | null;
  departements?: IDepartement[] | null;
  frereQuiInvites?: IFrereQuiInvite[] | null;
  besoins?: IBesoin[] | null;
  decisions?: IDecision[] | null;
}

export class Nouveau implements INouveau {
  constructor(
    public id?: number,
    public nomComplet?: string,
    public contact?: string | null,
    public trancheAge?: string | null,
    public situationMatrimoniale?: SituationMatrimoniale | null,
    public date?: dayjs.Dayjs | null,
    public impressionsDuCulte?: string | null,
    public sexe?: Sexe | null,
    public invitePar?: CanalInvitation | null,
    public communaute?: ICommunaute | null,
    public ville?: IVille | null,
    public quartier?: IQuartier | null,
    public culte?: ICulte | null,
    public departements?: IDepartement[] | null,
    public frereQuiInvites?: IFrereQuiInvite[] | null,
    public besoins?: IBesoin[] | null,
    public decisions?: IDecision[] | null
  ) {}
}

export function getNouveauIdentifier(nouveau: INouveau): number | undefined {
  return nouveau.id;
}
