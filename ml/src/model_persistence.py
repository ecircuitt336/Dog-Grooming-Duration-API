from joblib import dump, load

# Persists the object to the disk
def save_model(pipeline, path):
    dump(pipeline, path)

# Loads the object from the disk
def load_model(path):
    return load(path)