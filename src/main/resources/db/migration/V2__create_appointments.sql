CREATE TABLE appointments (
    id UUID NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    breed_id BIGINT NOT NULL REFERENCES breeds(id),
    weight_kg NUMERIC(5,2) NOT NULL
        CHECK (weight_kg > 0 AND weight_kg <= 200),
    coat_length VARCHAR(20) NOT NULL
        CHECK (coat_length IN ('SHORT', 'MEDIUM', 'LONG')),
    coat_texture VARCHAR(20) NOT NULL
        CHECK (coat_texture IN (
            'SMOOTH',
            'WAVY',
            'CURLY',
            'WIRY',
            'SILKY',
            'ROUGH'
        )),
    coat_structure VARCHAR(20) NOT NULL
        CHECK (coat_structure IN ('SINGLE', 'DOUBLE')),
    matting_severity VARCHAR(20) NOT NULL
        CHECK (matting_severity IN (
            'NONE',
            'MILD',
            'MODERATE',
            'SEVERE'
        )),
    behaviour VARCHAR(20) NOT NULL
        CHECK (behaviour IN (
            'VERY_BAD',
            'BAD',
            'OKAY',
            'GOOD',
            'VERY_GOOD'
        )),
    service VARCHAR(40) NOT NULL
        CHECK (service IN (
            'FULL_GROOM_AND_CLIP',
            'FULL_GROOM_AND_HAND_STRIP',
            'TIDY_UP',
            'BATH_AND_DRY'
        )),
    groomer_experience_years NUMERIC(4,1) NOT NULL
        CHECK (groomer_experience_years >= 0),
    estimator_version VARCHAR(50) NOT NULL
        CHECK (length(trim(estimator_version)) > 0),
    estimated_minutes INTEGER NOT NULL
        CHECK (estimated_minutes > 0),
    lower_bound_minutes INTEGER NOT NULL
        CHECK (
            lower_bound_minutes > 0
            AND lower_bound_minutes <= estimated_minutes
        ),
    upper_bound_minutes INTEGER NOT NULL
        CHECK (
            upper_bound_minutes > 0
            AND upper_bound_minutes >= estimated_minutes
        ),
    actual_duration_minutes INTEGER
        CHECK (
            actual_duration_minutes IS NULL
            OR actual_duration_minutes > 0
        ),
    completed_at TIMESTAMPTZ,
    CHECK (
        (actual_duration_minutes IS NULL AND completed_at IS NULL)
        OR
        (actual_duration_minutes IS NOT NULL AND completed_at IS NOT NULL)
    )
);