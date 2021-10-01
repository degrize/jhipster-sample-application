jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NouveauService } from '../service/nouveau.service';
import { INouveau, Nouveau } from '../nouveau.model';
import { ICommunaute } from 'app/entities/communaute/communaute.model';
import { CommunauteService } from 'app/entities/communaute/service/communaute.service';
import { IVille } from 'app/entities/ville/ville.model';
import { VilleService } from 'app/entities/ville/service/ville.service';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { QuartierService } from 'app/entities/quartier/service/quartier.service';
import { ICulte } from 'app/entities/culte/culte.model';
import { CulteService } from 'app/entities/culte/service/culte.service';
import { IDepartement } from 'app/entities/departement/departement.model';
import { DepartementService } from 'app/entities/departement/service/departement.service';
import { IFrereQuiInvite } from 'app/entities/frere-qui-invite/frere-qui-invite.model';
import { FrereQuiInviteService } from 'app/entities/frere-qui-invite/service/frere-qui-invite.service';
import { IBesoin } from 'app/entities/besoin/besoin.model';
import { BesoinService } from 'app/entities/besoin/service/besoin.service';
import { IDecision } from 'app/entities/decision/decision.model';
import { DecisionService } from 'app/entities/decision/service/decision.service';

import { NouveauUpdateComponent } from './nouveau-update.component';

describe('Component Tests', () => {
  describe('Nouveau Management Update Component', () => {
    let comp: NouveauUpdateComponent;
    let fixture: ComponentFixture<NouveauUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let nouveauService: NouveauService;
    let communauteService: CommunauteService;
    let villeService: VilleService;
    let quartierService: QuartierService;
    let culteService: CulteService;
    let departementService: DepartementService;
    let frereQuiInviteService: FrereQuiInviteService;
    let besoinService: BesoinService;
    let decisionService: DecisionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [NouveauUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(NouveauUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NouveauUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      nouveauService = TestBed.inject(NouveauService);
      communauteService = TestBed.inject(CommunauteService);
      villeService = TestBed.inject(VilleService);
      quartierService = TestBed.inject(QuartierService);
      culteService = TestBed.inject(CulteService);
      departementService = TestBed.inject(DepartementService);
      frereQuiInviteService = TestBed.inject(FrereQuiInviteService);
      besoinService = TestBed.inject(BesoinService);
      decisionService = TestBed.inject(DecisionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Communaute query and add missing value', () => {
        const nouveau: INouveau = { id: 456 };
        const communaute: ICommunaute = { id: 99714 };
        nouveau.communaute = communaute;

        const communauteCollection: ICommunaute[] = [{ id: 72015 }];
        jest.spyOn(communauteService, 'query').mockReturnValue(of(new HttpResponse({ body: communauteCollection })));
        const additionalCommunautes = [communaute];
        const expectedCollection: ICommunaute[] = [...additionalCommunautes, ...communauteCollection];
        jest.spyOn(communauteService, 'addCommunauteToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ nouveau });
        comp.ngOnInit();

        expect(communauteService.query).toHaveBeenCalled();
        expect(communauteService.addCommunauteToCollectionIfMissing).toHaveBeenCalledWith(communauteCollection, ...additionalCommunautes);
        expect(comp.communautesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Ville query and add missing value', () => {
        const nouveau: INouveau = { id: 456 };
        const ville: IVille = { id: 68658 };
        nouveau.ville = ville;

        const villeCollection: IVille[] = [{ id: 73595 }];
        jest.spyOn(villeService, 'query').mockReturnValue(of(new HttpResponse({ body: villeCollection })));
        const additionalVilles = [ville];
        const expectedCollection: IVille[] = [...additionalVilles, ...villeCollection];
        jest.spyOn(villeService, 'addVilleToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ nouveau });
        comp.ngOnInit();

        expect(villeService.query).toHaveBeenCalled();
        expect(villeService.addVilleToCollectionIfMissing).toHaveBeenCalledWith(villeCollection, ...additionalVilles);
        expect(comp.villesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Quartier query and add missing value', () => {
        const nouveau: INouveau = { id: 456 };
        const quartier: IQuartier = { id: 85785 };
        nouveau.quartier = quartier;

        const quartierCollection: IQuartier[] = [{ id: 57499 }];
        jest.spyOn(quartierService, 'query').mockReturnValue(of(new HttpResponse({ body: quartierCollection })));
        const additionalQuartiers = [quartier];
        const expectedCollection: IQuartier[] = [...additionalQuartiers, ...quartierCollection];
        jest.spyOn(quartierService, 'addQuartierToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ nouveau });
        comp.ngOnInit();

        expect(quartierService.query).toHaveBeenCalled();
        expect(quartierService.addQuartierToCollectionIfMissing).toHaveBeenCalledWith(quartierCollection, ...additionalQuartiers);
        expect(comp.quartiersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Culte query and add missing value', () => {
        const nouveau: INouveau = { id: 456 };
        const culte: ICulte = { id: 98823 };
        nouveau.culte = culte;

        const culteCollection: ICulte[] = [{ id: 85280 }];
        jest.spyOn(culteService, 'query').mockReturnValue(of(new HttpResponse({ body: culteCollection })));
        const additionalCultes = [culte];
        const expectedCollection: ICulte[] = [...additionalCultes, ...culteCollection];
        jest.spyOn(culteService, 'addCulteToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ nouveau });
        comp.ngOnInit();

        expect(culteService.query).toHaveBeenCalled();
        expect(culteService.addCulteToCollectionIfMissing).toHaveBeenCalledWith(culteCollection, ...additionalCultes);
        expect(comp.cultesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Departement query and add missing value', () => {
        const nouveau: INouveau = { id: 456 };
        const departements: IDepartement[] = [{ id: 2049 }];
        nouveau.departements = departements;

        const departementCollection: IDepartement[] = [{ id: 66042 }];
        jest.spyOn(departementService, 'query').mockReturnValue(of(new HttpResponse({ body: departementCollection })));
        const additionalDepartements = [...departements];
        const expectedCollection: IDepartement[] = [...additionalDepartements, ...departementCollection];
        jest.spyOn(departementService, 'addDepartementToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ nouveau });
        comp.ngOnInit();

        expect(departementService.query).toHaveBeenCalled();
        expect(departementService.addDepartementToCollectionIfMissing).toHaveBeenCalledWith(
          departementCollection,
          ...additionalDepartements
        );
        expect(comp.departementsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call FrereQuiInvite query and add missing value', () => {
        const nouveau: INouveau = { id: 456 };
        const frereQuiInvites: IFrereQuiInvite[] = [{ id: 23375 }];
        nouveau.frereQuiInvites = frereQuiInvites;

        const frereQuiInviteCollection: IFrereQuiInvite[] = [{ id: 8100 }];
        jest.spyOn(frereQuiInviteService, 'query').mockReturnValue(of(new HttpResponse({ body: frereQuiInviteCollection })));
        const additionalFrereQuiInvites = [...frereQuiInvites];
        const expectedCollection: IFrereQuiInvite[] = [...additionalFrereQuiInvites, ...frereQuiInviteCollection];
        jest.spyOn(frereQuiInviteService, 'addFrereQuiInviteToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ nouveau });
        comp.ngOnInit();

        expect(frereQuiInviteService.query).toHaveBeenCalled();
        expect(frereQuiInviteService.addFrereQuiInviteToCollectionIfMissing).toHaveBeenCalledWith(
          frereQuiInviteCollection,
          ...additionalFrereQuiInvites
        );
        expect(comp.frereQuiInvitesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Besoin query and add missing value', () => {
        const nouveau: INouveau = { id: 456 };
        const besoins: IBesoin[] = [{ id: 10788 }];
        nouveau.besoins = besoins;

        const besoinCollection: IBesoin[] = [{ id: 17942 }];
        jest.spyOn(besoinService, 'query').mockReturnValue(of(new HttpResponse({ body: besoinCollection })));
        const additionalBesoins = [...besoins];
        const expectedCollection: IBesoin[] = [...additionalBesoins, ...besoinCollection];
        jest.spyOn(besoinService, 'addBesoinToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ nouveau });
        comp.ngOnInit();

        expect(besoinService.query).toHaveBeenCalled();
        expect(besoinService.addBesoinToCollectionIfMissing).toHaveBeenCalledWith(besoinCollection, ...additionalBesoins);
        expect(comp.besoinsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Decision query and add missing value', () => {
        const nouveau: INouveau = { id: 456 };
        const decisions: IDecision[] = [{ id: 24474 }];
        nouveau.decisions = decisions;

        const decisionCollection: IDecision[] = [{ id: 29591 }];
        jest.spyOn(decisionService, 'query').mockReturnValue(of(new HttpResponse({ body: decisionCollection })));
        const additionalDecisions = [...decisions];
        const expectedCollection: IDecision[] = [...additionalDecisions, ...decisionCollection];
        jest.spyOn(decisionService, 'addDecisionToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ nouveau });
        comp.ngOnInit();

        expect(decisionService.query).toHaveBeenCalled();
        expect(decisionService.addDecisionToCollectionIfMissing).toHaveBeenCalledWith(decisionCollection, ...additionalDecisions);
        expect(comp.decisionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const nouveau: INouveau = { id: 456 };
        const communaute: ICommunaute = { id: 81815 };
        nouveau.communaute = communaute;
        const ville: IVille = { id: 83206 };
        nouveau.ville = ville;
        const quartier: IQuartier = { id: 29836 };
        nouveau.quartier = quartier;
        const culte: ICulte = { id: 70561 };
        nouveau.culte = culte;
        const departements: IDepartement = { id: 15415 };
        nouveau.departements = [departements];
        const frereQuiInvites: IFrereQuiInvite = { id: 80830 };
        nouveau.frereQuiInvites = [frereQuiInvites];
        const besoins: IBesoin = { id: 82599 };
        nouveau.besoins = [besoins];
        const decisions: IDecision = { id: 9890 };
        nouveau.decisions = [decisions];

        activatedRoute.data = of({ nouveau });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(nouveau));
        expect(comp.communautesSharedCollection).toContain(communaute);
        expect(comp.villesSharedCollection).toContain(ville);
        expect(comp.quartiersSharedCollection).toContain(quartier);
        expect(comp.cultesSharedCollection).toContain(culte);
        expect(comp.departementsSharedCollection).toContain(departements);
        expect(comp.frereQuiInvitesSharedCollection).toContain(frereQuiInvites);
        expect(comp.besoinsSharedCollection).toContain(besoins);
        expect(comp.decisionsSharedCollection).toContain(decisions);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Nouveau>>();
        const nouveau = { id: 123 };
        jest.spyOn(nouveauService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ nouveau });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: nouveau }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(nouveauService.update).toHaveBeenCalledWith(nouveau);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Nouveau>>();
        const nouveau = new Nouveau();
        jest.spyOn(nouveauService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ nouveau });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: nouveau }));
        saveSubject.complete();

        // THEN
        expect(nouveauService.create).toHaveBeenCalledWith(nouveau);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Nouveau>>();
        const nouveau = { id: 123 };
        jest.spyOn(nouveauService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ nouveau });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(nouveauService.update).toHaveBeenCalledWith(nouveau);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCommunauteById', () => {
        it('Should return tracked Communaute primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCommunauteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackVilleById', () => {
        it('Should return tracked Ville primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVilleById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackQuartierById', () => {
        it('Should return tracked Quartier primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackQuartierById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCulteById', () => {
        it('Should return tracked Culte primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCulteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDepartementById', () => {
        it('Should return tracked Departement primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDepartementById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackFrereQuiInviteById', () => {
        it('Should return tracked FrereQuiInvite primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFrereQuiInviteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackBesoinById', () => {
        it('Should return tracked Besoin primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBesoinById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDecisionById', () => {
        it('Should return tracked Decision primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDecisionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedDepartement', () => {
        it('Should return option if no Departement is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedDepartement(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Departement for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedDepartement(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Departement is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedDepartement(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });

      describe('getSelectedFrereQuiInvite', () => {
        it('Should return option if no FrereQuiInvite is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedFrereQuiInvite(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected FrereQuiInvite for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedFrereQuiInvite(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this FrereQuiInvite is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedFrereQuiInvite(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });

      describe('getSelectedBesoin', () => {
        it('Should return option if no Besoin is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedBesoin(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Besoin for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedBesoin(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Besoin is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedBesoin(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });

      describe('getSelectedDecision', () => {
        it('Should return option if no Decision is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedDecision(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Decision for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedDecision(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Decision is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedDecision(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
