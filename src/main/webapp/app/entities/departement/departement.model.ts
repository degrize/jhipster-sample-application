import { IImageCulte } from 'app/entities/image-culte/image-culte.model';
import { IGem } from 'app/entities/gem/gem.model';
import { INouveau } from 'app/entities/nouveau/nouveau.model';
import { IFrereQuiInvite } from 'app/entities/frere-qui-invite/frere-qui-invite.model';

export interface IDepartement {
  id?: number;
  nom?: string;
  shortName?: string;
  nomResponsable?: string | null;
  videoIntroductionContentType?: string | null;
  videoIntroduction?: string | null;
  contactResponsable?: string | null;
  description?: string | null;
  couleur1?: string | null;
  couleur2?: string | null;
  imageCultes?: IImageCulte[] | null;
  gems?: IGem[] | null;
  nouveaus?: INouveau[] | null;
  frereQuiInvites?: IFrereQuiInvite[] | null;
}

export class Departement implements IDepartement {
  constructor(
    public id?: number,
    public nom?: string,
    public shortName?: string,
    public nomResponsable?: string | null,
    public videoIntroductionContentType?: string | null,
    public videoIntroduction?: string | null,
    public contactResponsable?: string | null,
    public description?: string | null,
    public couleur1?: string | null,
    public couleur2?: string | null,
    public imageCultes?: IImageCulte[] | null,
    public gems?: IGem[] | null,
    public nouveaus?: INouveau[] | null,
    public frereQuiInvites?: IFrereQuiInvite[] | null
  ) {}
}

export function getDepartementIdentifier(departement: IDepartement): number | undefined {
  return departement.id;
}
