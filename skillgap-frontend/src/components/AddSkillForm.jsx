import { useEffect, useState } from "react";
import { getSkillCatalog } from "../api/skillCatalogApi";
import { addMySkill } from "../api/skillApi";

function AddSkillForm({ onSkillAdded }) {
  const [skills, setSkills] = useState([]);
  const [selectedSkillId, setSelectedSkillId] = useState("");
  const [proficiency, setProficiency] = useState(1);

  const [loading, setLoading] = useState(true);
  const [adding, setAdding] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  useEffect(() => {
    async function loadSkillCatalog() {
      try {
        const data = await getSkillCatalog();
        setSkills(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }

    loadSkillCatalog();
  }, []);

  async function handleSubmit(event) {
    event.preventDefault();

    if (!selectedSkillId) {
      setError("Please select a skill.");
      return;
    }

    setError("");
    setSuccess("");
    setAdding(true);

    try {
      await addMySkill(
        selectedSkillId,
        Number(proficiency)
      );

      setSuccess("Skill added successfully.");
      setSelectedSkillId("");
      setProficiency(1);

      if (onSkillAdded) {
        onSkillAdded();
      }
} catch (err) {
  if (err.message.includes("already has this skill")) {
    setError("You already have this skill in your profile.");
  } else {
    setError(err.message);
  }
} finally {
      setAdding(false);
    }
  }
  if (loading) {
    return <p className="page-loading">Loading skills...</p>;
  }

  return (
    <div className="add-skill-form">
      <h2>Add Skill</h2>
      <p className="form-description">
        Add a skill and tell us your current proficiency level.
      </p>

      <form onSubmit={handleSubmit}>

        <div className="form-group">
          <label htmlFor="skill">
            Skill
          </label>

          <select
            id="skill"
            value={selectedSkillId}
            onChange={(event) =>
              setSelectedSkillId(event.target.value)
            }
          >
            <option value="">
              Select a skill
            </option>

            {skills.map((skill) => (
              <option
                key={skill.id}
                value={skill.id}
              >
                {skill.name}
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="proficiency">
            Proficiency
          </label>

          <select
            id="proficiency"
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

        {error && (
          <p className="error-message">{error}</p>
        )}

        {success && (
          <p className="success-message">{success}</p>
        )}

        <button
          type="submit"
          className="btn btn-primary"
          disabled={adding}
        >
          {adding ? "Adding..." : "Add Skill"}
        </button>

      </form>
    </div>
  );
}

export default AddSkillForm;