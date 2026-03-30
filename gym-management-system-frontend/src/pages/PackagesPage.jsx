import { useEffect, useMemo, useState } from 'react';
import toast from 'react-hot-toast';
import { api, unwrapApi } from '../app/apiClient';
import { useAuth } from '../app/AuthContext';

function PurchaseModal({ pkg, onClose, onConfirm }) {
  const [paymentMethod, setPaymentMethod] = useState('UPI');
  const [amount, setAmount] = useState(pkg?.price ?? 0);

  if (!pkg) return null;

  return (
    <div className="modal-overlay" role="dialog" aria-modal="true">
      <div className="modal">
        <div className="modal-header">
          <div>
            <div className="modal-title">Purchase {pkg.packageName}</div>
            <div className="muted">
              {pkg.durationMonths} months | Price: {pkg.price}
            </div>
          </div>
          <button className="btn btn-ghost" onClick={onClose} type="button">
            Close
          </button>
        </div>

        <div className="modal-body">
          <label className="label">
            Payment Method
            <select className="input" value={paymentMethod} onChange={(e) => setPaymentMethod(e.target.value)}>
              <option value="UPI">UPI</option>
              <option value="CREDIT_CARD">CREDIT_CARD</option>
            </select>
          </label>

          <label className="label">
            Amount
            <input
              className="input"
              type="number"
              step="0.01"
              value={amount}
              onChange={(e) => setAmount(Number(e.target.value))}
            />
          </label>
        </div>

        <div className="modal-footer">
          <button className="btn btn-primary" onClick={() => onConfirm({ paymentMethod, amount })} type="button">
            Pay
          </button>
          <button className="btn btn-secondary" onClick={onClose} type="button">
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
}

export default function PackagesPage() {
  const { userId } = useAuth();

  const [packages, setPackages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedPkg, setSelectedPkg] = useState(null);

  const fetchPackages = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await api.get('/api/package/active');
      const list = unwrapApi(res.data) || [];
      setPackages(list);
    } catch (err) {
      setError(err?.response?.data?.message || err?.message || 'Failed to load packages');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPackages();
  }, []);

  const canPay = useMemo(() => userId != null && selectedPkg != null, [userId, selectedPkg]);

  const onConfirm = async ({ paymentMethod, amount }) => {
    if (!canPay) return;
    try {
      const payload = {
        memberId: userId,
        packageId: selectedPkg.packageId,
        amount,
        paymentMethod,
      };
      await api.post('/api/payment/pay', payload);
      toast.success('Payment processed successfully');
      setSelectedPkg(null);
    } catch (err) {
      toast.error(err?.response?.data?.message || err?.message || 'Payment failed');
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Packages</h1>
        <div className="muted">Choose a subscription package</div>
      </div>

      {loading ? <div className="muted">Loading packages...</div> : null}
      {error ? <div className="alert alert-error">{error}</div> : null}

      <div className="grid">
        {packages.map((pkg) => (
          <div key={pkg.packageId} className="card grid-card">
            <div className="card-title">{pkg.packageName}</div>
            <div className="muted">
              {pkg.durationMonths} months | Price: {pkg.price}
            </div>
            {pkg.benefits ? <div className="mt-8 benefits">{pkg.benefits}</div> : null}

            <div className="mt-16">
              <button className="btn btn-primary" onClick={() => setSelectedPkg(pkg)} type="button">
                Buy
              </button>
            </div>
          </div>
        ))}
      </div>

      <PurchaseModal
        key={selectedPkg?.packageId || 'none'}
        pkg={selectedPkg}
        onClose={() => setSelectedPkg(null)}
        onConfirm={onConfirm}
      />
    </div>
  );
}

