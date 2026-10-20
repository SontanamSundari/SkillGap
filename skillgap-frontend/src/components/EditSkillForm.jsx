import { useState } from "react";
import { updateMySkill } from "../api/skillApi";

function EditSkillForm({ skill, onUpdated, onCancel }) {
  const [proficiency, setProficiency] = useState(skill.proficiency);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  async function handleSubmit(event) {
    event.preventDefault();

    setError("");
    setSaving(true);

    try {
      await updateMySkill(
        skill.skillId,
        Number(proficiency)
      );

      if (onUpdated) {
        onUpdated();
      }
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  }


  return (
    <form className="edit-skill-form" onSubmit={handleSubmit}>

      <div className="form-group">
        <label htmlFor={`edit-${skill.skillId}`}>
          Proficiency
        </label>

        <select
          id={`edit-${skill.skillId}`}
          value={proficiency}
          onChange={(event) =>
            setProficiency(event.target.value)
          }
        >
          <option value="1">1 - Beginner</option>
          <option value="2">2 - Basic</option>
          <option value="3">3 - Intermediate</option>
          <option value="4">4 - Advanced</option>
          <option value="5">5 - Expert</option>
        </select>
      </div>

      {error && <p className="error-message">{error}</p>}

      <div className="edit-actions">
        <button
          type="submit"
          className="btn btn-primary"
          disabled={saving}
        >
          {saving ? "Saving..." : "Save"}
        </button>

        <button
          type="button"
          className="btn btn-secondary"
          onClick={onCancel}
          disabled={saving}
        >
          Cancel
        </button>
      </div>

    </form>
  );
}

export default EditSkillForm;