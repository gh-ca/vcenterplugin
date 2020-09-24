import { Observable } from 'rxjs';
import { AccordionService } from './../../providers/accordion.service';
import { StepperModel } from '../models/stepper.model';
export declare class StepperService extends AccordionService {
    readonly panelsCompleted: Observable<boolean>;
    protected accordion: StepperModel;
    private _activeStepChanges;
    readonly activeStep: Observable<string>;
    resetPanels(): void;
    setPanelsWithErrors(ids: string[]): void;
    navigateToNextPanel(currentPanelId: string, currentPanelValid?: boolean): void;
    overrideInitialPanel(panelId: string): void;
    private updateNextStep;
    private getAllCompletedPanelChanges;
}
